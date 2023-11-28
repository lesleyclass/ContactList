package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.NEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.util.getParcelableExtraCompat

class ContactActivity: AppCompatActivity() {
    private val activityContactBinding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityContactBinding.root)
        setSupportActionBar(activityContactBinding.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)

        val contactExtra = intent.getParcelableExtraCompat(NEW_CONTACT, Contact::class.java)
        contactExtra?.let { contact->
            val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
            with(activityContactBinding){
                if (viewContact) {
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = GONE
                }
            }
            with(activityContactBinding){
                nameEt.setText(contact.name)
                addressEt.setText(contact.address)
                phoneEt.setText(contact.phone)
                emailEt.setText(contact.email)
            }
        }

        with(activityContactBinding){
            saveBt.setOnClickListener{
                val contact = Contact(
                    id = contactExtra?.id ?:hashCode(),
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString(),
                )

                val resultIntent = Intent()
                resultIntent.putExtra(NEW_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}