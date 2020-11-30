package com.vision2020.ui.settings.group
import BaseFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.AddStudentAdapter
import com.vision2020.data.response.StudentListing
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.AppConstant.SUCCESS
import com.vision2020.utils.isAppConnected
import com.vision2020.utils.progressDialog
import com.vision2020.utils.responseHandler
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_add_student.*
class AddStudentFragment : BaseFragment<AddGroupViewModel>(),AddStudentAdapter.OnItemClick {
    val arg : AddStudentFragmentArgs by navArgs()
    private var leaderId:String =""
    private var list = arrayListOf<StudentListing.Data>()
    private var selectedList = arrayListOf<String>()
    private var isCheckAll:Boolean = false
    override val layoutId: Int
        get() = R.layout.fragment_add_student
    override val viewModel: AddGroupViewModel
        get() = ViewModelProvider(this).get(AddGroupViewModel::class.java)

    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
        getStudent()
    }

    private fun getStudent() {

        if(mContext.isAppConnected()){
            progress!!.show()
            mViewModel!!.getStudent().observe(viewLifecycleOwner, Observer {
                progress!!.hide()
                if(it.status_code == CODE_SUCCESS && it.data!=null){
                    list = it.data!!
                    setAdapter()
                }else{
                    mContext.responseHandler(it.status_code,it.message)
                }
            })
        }
    }

    private fun setAdapter() {
       rvAddStuToGroup.layoutManager = LinearLayoutManager(mContext)
       rvAddStuToGroup.adapter = AddStudentAdapter(mContext,list,isCheckAll,this)
    }

    override fun initListeners() {
        checkBoxAll.setOnClickListener {
          if(checkBoxAll.isChecked){
              isCheckAll = true
              setAdapter()
              selectedList.clear()
              for(i in list){
                  selectedList.add(i.id.toString())
              }
          }else{
              isCheckAll = false
              setAdapter()
              selectedList.clear()
          }
        }
        btnAddStu.setOnClickListener {
            when {
                selectedList.isEmpty() -> {
                    mContext.showToastMsg(getString(R.string.error_select_student), ERROR)
                }
                selectedList.size > 1 &&  leaderId.trim().isEmpty() -> {
                    mContext.showToastMsg(getString(R.string.error_group_leader), ERROR)
                }
                selectedList.size==1 && leaderId.trim().isEmpty()->{
                 leaderId = selectedList.joinToString()
                 addMemberToGroup(selectedList,leaderId)
                 leaderId =""
                }
                else -> {
                    addMemberToGroup(selectedList,leaderId)
                    leaderId=""
                }
            }

        }
    }
    override fun getItemCheck(pos: String) {
         selectedList.add(pos)
    }
    override fun getItemUnCheck(student: String) {
           selectedList.remove(student)
    }
    override fun addMember(id:String) {
        val list = arrayListOf<String>()
        list.add(id)
        addMemberToGroup(list,id)
    }

    override fun onItemSelected(id: String) {
      leaderId = id
    }

    private fun addMemberToGroup(id: ArrayList<String>,leaderId:String) {
        if(mContext.isAppConnected()){
            progress!!.show()
            mViewModel!!.addToGroup(arg.group,leaderId,id).observe(viewLifecycleOwner, Observer {
               progress!!.hide()
                selectedList.clear()
                if(it.status_code== CODE_SUCCESS){
                    mContext.showToastMsg(getString(R.string.student_added), SUCCESS)
                    when {
                        isCheckAll -> {
                            requireActivity().onBackPressed()
                        }
                        else -> {
                            getStudent()
                        }
                    }
                }else{
                    mContext.responseHandler(it.status_code,it.message)
                }
            })
        }



    }

}
