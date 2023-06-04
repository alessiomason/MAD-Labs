package it.polito.mad.playgroundsreservations.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.ContactCard
import it.polito.mad.playgroundsreservations.reservations.ViewModel



class ContactsAdapter(val context: Context, private  val contactList: ArrayList<ContactCard>)
    : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    class ContactsViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card_view)
        val name: TextView = view.findViewById(R.id.textName)
        val email: TextView = view.findViewById(R.id.textPhone)
        val age: TextView = view.findViewById(R.id.textBio)
        val profileImage: ImageView = view.findViewById(R.id.imgProfileImage)
        val addFriend:Button = view.findViewById(R.id.btnAddFriend)
    }

      //private  val contactList: ArrayList<LiveData<User?>>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val contactView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_contacts,parent,false)

        return ContactsViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val list = contactList[position]
        // val userFriends =  viewModel.getUserInfo(Global.userId!!).value!!.friends

        holder.card.setOnClickListener {
            val intent = Intent(this.context, ShowProfileActivity::class.java)
            intent.putExtra("reservationCreatorId", list.id)
            this.context.startActivity(intent)
        }

        holder.name.text = list.profileName

        holder.email.text = list.profileEmail

        holder.age.text = list.profileBio

        val storageReference = Firebase.storage.reference.child("profileImages/${list.id}")

        Glide.with(this.context)
            .load(storageReference)
            .placeholder(R.drawable.user_profile)
            .into(holder.profileImage)




            if(list.isFriend)
            {
                holder.addFriend.text = "Delete"
                holder.addFriend.setOnClickListener {
                    val friendReference =
                        FirebaseFirestore.getInstance().collection(ViewModel.usersCollectionPath)
                            .document(list.id)


                    FirebaseFirestore.getInstance().collection(ViewModel.usersCollectionPath)
                        .document(Global.userId!!)
                        .update("friends", FieldValue.arrayRemove(friendReference))
                    list.isFriend = false
                }
            }else{

                holder.addFriend.text = "Add"
                holder.addFriend.setOnClickListener {
                val friendReference =
                    FirebaseFirestore.getInstance().collection(ViewModel.usersCollectionPath)
                        .document(list.id)

                FirebaseFirestore.getInstance().collection(ViewModel.usersCollectionPath)
                    .document(Global.userId!!)
                    .update("friends", FieldValue.arrayUnion(friendReference))
                list.isFriend = true
                }
            }

        }

        }



