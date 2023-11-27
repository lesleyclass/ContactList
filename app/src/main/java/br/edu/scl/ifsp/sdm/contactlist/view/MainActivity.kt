package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.NEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.util.getParcelableExtraCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity() {
    private val contactsList: MutableList<Contact> = mutableListOf()
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.contactsRv)
    }

    private val contactAdapter: RecyclerView.Adapter<ContactAdapter.ViewHolder> by lazy {
        ContactAdapter(contactsList)
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
                    contact?.also {
                        if (contactsList.any { it.id == contact.id }) {
                            //Editar contato
                        } else {
                            contactsList.add(contact)
                            contactAdapter.notifyItemChanged(contactsList.size-1)
                        }
                    }
                }
            }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        fillContactsList()
        recyclerView.adapter = contactAdapter
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
    private fun fillContactsList(){
        for(i in 1..10){
            contactsList.add(
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

    override fun onResume() {
        super.onResume()
        // Fetch newItems from database
//        recyclerView.adapter?.updateItems(newItems)
//        recyclerView.adapter?.notifyDataSetChanged()
    }
}