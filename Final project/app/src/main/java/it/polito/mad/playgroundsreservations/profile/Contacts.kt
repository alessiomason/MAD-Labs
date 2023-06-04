package it.polito.mad.playgroundsreservations.profile


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.ContactCard
import it.polito.mad.playgroundsreservations.reservations.ViewModel

class Contacts: AppCompatActivity() {

    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contactLayoutManager: RecyclerView.LayoutManager
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var queryTerm: String
    private val contactInfo = ArrayList<ContactCard>()
    private var register: ListenerRegistration? = null
    private lateinit var userFriends: List<DocumentReference>
    val viewModel by viewModels<ViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contacts)
        this.title = resources?.getString(R.string.search_people)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo(Global.userId!!).observe(this) { user ->

            if (user != null) {


                userFriends = user.friends
                contactsRecyclerView = findViewById(R.id.contactsRecyclerView)
                contactsRecyclerView.setHasFixedSize(true)
                val searchPeople = findViewById<SearchView>(R.id.searchPeople)
                searchPeople.clearFocus()
                searchPeople.isSubmitButtonEnabled = true
                searchPeople.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            queryTerm = query
                            if (queryTerm.isNotEmpty()) {
                                searchUsers()
                            }
                        }
                        return true
                    }


                    override fun onQueryTextChange(newText: String?): Boolean {

                        if (newText != null) {
                            queryTerm = newText
                            if (queryTerm.isNotEmpty()) {
                                searchUsers()
                            } else {
                                contactsRecyclerView.visibility = View.GONE
                            }
                        }
                        return true

                    }

                })
                contactLayoutManager = LinearLayoutManager(this?.applicationContext)
            }

        }


    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_anim, R.anim.fade_out)
    }

    private fun searchUsers() {

        register = FirebaseFirestore.getInstance().collection("users").orderBy("fullName")
            .startAt(queryTerm.uppercase()).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("onError", "Some Error Occured")
            } else {
                if (!snapshot?.isEmpty!!) {
                    contactInfo.clear()
                    val searchList = snapshot.documents
                    val filteredList = searchList.filter { friend ->
                        friend.getString("fullName").toString().startsWith(queryTerm, true)


                    }

                    if (filteredList.isEmpty()) {
                        contactsRecyclerView.visibility = View.GONE

                    } else {
                        contactsRecyclerView.visibility = View.VISIBLE

                        for (search in filteredList) {

                            var isFriend = false

                            if (FirebaseAuth.getInstance().currentUser!!.uid == search.id) {

                                Log.d("onSuccess", "User Running The App")

                            } else {

                               viewModel.getUserInfo(search.id).observe(this) {
                                    val userName = it?.fullName
                                    var userPhone = it?.phone
                                    var userBio = it?.bio
                                    if (userFriends.contains(search.reference)) {
                                        isFriend = true
                                    }


                                    val contact = ContactCard(
                                        search.id,
                                        profileName = userName,
                                        profileEmail = userPhone,
                                        profileBio = userBio,
                                        isFriend = isFriend
                                    )

                                    contactInfo.add(contact)
                                    contactsAdapter = ContactsAdapter(this, contactInfo)
                                    contactsRecyclerView.adapter = contactsAdapter
                                    contactsRecyclerView.layoutManager = contactLayoutManager

                                }
                            }
                        }
                        contactInfo.clear()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        register?.remove()
        super.onDestroy()
    }

}





