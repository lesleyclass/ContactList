package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ContactItemBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.view.OnContactClickListener
import kotlin.math.absoluteValue

class ContactAdapter(
    private val contactList: List<Contact>,
    val onContactClickListener: OnContactClickListener,
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(contactItemBinding: ContactItemBinding) :
        RecyclerView.ViewHolder(contactItemBinding.root) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val emailTv: TextView = itemView.findViewById(R.id.emailTv)
        val phoneTv: TextView = itemView.findViewById(R.id.phoneTv)

        init {
            contactItemBinding.root.apply {
                setOnCreateContextMenuListener { menu, _, _ ->
                    (onContactClickListener as AppCompatActivity).menuInflater.inflate(
                        R.menu.context_menu_main,
                        menu
                    )
                    menu.findItem(R.id.removeContactMi).setOnMenuItemClickListener {
                        onContactClickListener.onRemoveContactMenuItemClick(adapterPosition.absoluteValue)
                        true
                    }
                    menu.findItem(R.id.editContactMi).setOnMenuItemClickListener {
                        onContactClickListener.onEditContactMenuItemClick(adapterPosition)
                        true
                    }
                }
                itemView.setOnClickListener {
                    onContactClickListener.onContactClick(adapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
        val binding = ContactItemBinding.inflate(view)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contact = contactList[position]
        viewHolder.nameTv.text = contact.name
        viewHolder.emailTv.text = contact.email
        viewHolder.phoneTv.text = contact.phone
    }

    override fun getItemCount() = contactList.size

}