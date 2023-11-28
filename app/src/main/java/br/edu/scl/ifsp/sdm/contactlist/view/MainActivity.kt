package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.NEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.util.getParcelableExtraCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val contactList: MutableList<Contact> = mutableListOf()
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val recyclerView: RecyclerView by lazy {
        amb.contactsRv
    }

    private val contactAdapter: RecyclerView.Adapter<ContactAdapter.ViewHolder> by lazy {
        ContactAdapter(contactList, this)
    }

    private lateinit var contactActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        contactActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val contact = result.data?.getParcelableExtraCompat(NEW_CONTACT, Contact::class.java)
                    contact?.also { newOrEditedContact ->
                        if (contactList.any { it.id == newOrEditedContact.id }) {
                            val position = contactList.indexOfFirst { it.id == newOrEditedContact.id }
                            contactList[position] = newOrEditedContact
                            contactAdapter.notifyItemChanged(position)
                        } else {
                            contactList.add(newOrEditedContact)
                            contactAdapter.notifyItemChanged(contactList.size-1)
                        }
                    }
                }
            }

        fillContactsList()
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addContactMi -> {
                contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onRemoveContactMenuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, getString(R.string.contact_removed_toast_text), Toast.LENGTH_SHORT).show()

    }
    override fun onEditContactMenuItemClick(position: Int) {
        contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(NEW_CONTACT, contactList[position])
        })
    }

    override fun onContactClick(position: Int) {
        Intent(this, ContactActivity::class.java).apply {
            putExtra(NEW_CONTACT, contactList[position])
            putExtra(VIEW_CONTACT, true)
        }.also {
            startActivity(it)
        }
    }

    private fun fillContactsList(){
        for(i in 1..10){
            contactList.add(
                Contact(
                    id = i,
                    name = "Nome $i",
                    address = "Endereço $i",
                    phone = "Telefone $i",
                    email = "E-mail $i",
                )
            )
        }
    }
}