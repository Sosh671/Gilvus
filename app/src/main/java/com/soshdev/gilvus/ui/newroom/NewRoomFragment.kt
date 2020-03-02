package com.soshdev.gilvus.ui.newroom

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentNewroomBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import com.soshdev.gilvus.util.ContactsHelper
import com.soshdev.gilvus.util.showSnackbar
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class NewRoomFragment : BaseFragment() {

    private lateinit var binding: FragmentNewroomBinding
    private val vm: NewRoomViewModel by viewModel()
    private val adapter = NewRoomAdapter {contact ->
        contact?.let {  vm.selectContact(it)  }?: binding.appBar.showSnackbar(R.string.contact_not_registered)
    }
    private val REQUEST_READ_CONTACTS = 22

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar, R.string.new_room)
        initRecyclerView()
        getContactList()

        vm.roomTitle.observe(viewLifecycleOwner, Observer { binding.edTitle.setText(it)} )

        binding.fabSave.setOnClickListener { vm.addRoom() }
    }

    private fun initRecyclerView() = with(binding) {
        val llm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerContacts.layoutManager = llm
        recyclerContacts.adapter = adapter
    }

    private fun getContactList() {
        context?.let { ctx ->
            if (!checkPermission(ctx)) {
                requestPermission()
            } else {
                val helper = ContactsHelper(ctx)
                val list = helper.getContacts()

                val contacts = list.mapNotNull { c -> c.phone }.toTypedArray()
                userToken?.let { vm.checkMyContactsExist(it, contacts) }
            }
        }
    }

    private fun checkPermission(ctx: Context) = ActivityCompat.checkSelfPermission(
        ctx, Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_CONTACTS),
            REQUEST_READ_CONTACTS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactList()
                } else {
                    // permission denied,Disable the functionality that depends on this permission.
                    Timber.d("no permission")
                    // todo no permission given
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}