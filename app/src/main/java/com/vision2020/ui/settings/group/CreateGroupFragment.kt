package com.vision2020.ui.settings.group
import BaseFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.vision2020.R
import com.vision2020.adapter.GroupListAdapter
import com.vision2020.adapter.StudentListAdapter
import com.vision2020.data.Student
import com.vision2020.data.response.GroupListing
import com.vision2020.data.response.StudentListing
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.isAppConnected
import com.vision2020.utils.progressDialog
import com.vision2020.utils.responseHandler
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class CreateGroupFragment : BaseFragment<GroupViewModel>(), StudentListAdapter.OnItemClick,GroupListAdapter.OnItemNavigation {
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private var isSelectAllChecked:Boolean = false
    var groupList = arrayListOf<GroupListing.Data>()
    var studentList = arrayListOf<StudentListing.Data>()
    var selectedStudentList = arrayListOf<Student>()
    override val layoutId: Int
        get() = R.layout.fragment_create_group
    override val viewModel: GroupViewModel
        get() = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
        if(mContext.isAppConnected()){
            progress!!.show()
             getGroups()
             // for Student
             mViewModel!!.getStudent().observe(this, Observer {
              if(it!=null){
                  if(it.status_code== CODE_SUCCESS && it.data!=null){
                     studentList = it.data!!
                      setStudentAdapter()
                  }else{
                      mContext.responseHandler(it.status_code,it.message)
                  }
              }else{
                  mContext.showToastMsg(getString(R.string.server_error),2)
              }
          })

        }

    }

    private fun getGroups() {
        mViewModel!!.getGroups().observe(viewLifecycleOwner, Observer {
            progress!!.dismiss()
            if(it!=null){
                if(it.status_code== CODE_SUCCESS && it.data!=null){
                    groupList = it.data!!
                    setGroupAdapter()
                }else{
                    mContext.responseHandler(it.status_code,it.message)
                }
            }else{
                mContext.showToastMsg(getString(R.string.server_error),2)
            }

        })
    }

    private fun setGroupAdapter() {
        if(groupList.isEmpty()){
            mContext.showToastMsg(getString(R.string.no_group_found),2)
        }else{
            rvGroupListing.layoutManager =LinearLayoutManager(mContext)
            rvGroupListing.adapter = GroupListAdapter(mContext,groupList,this)
        }

    }

    private fun setStudentAdapter() {
        rvStudentListing.layoutManager = LinearLayoutManager(mContext)
        rvStudentListing.adapter = StudentListAdapter(mContext,studentList,isSelectAllChecked,this)
    }

    override fun initListeners() {
        btnCreateGroup.setOnClickListener {
            if(editTextGroupName.text!!.trim().isEmpty()){
                mContext.showToastMsg(getString(R.string.error_group_name),2)
            }else{
                if(mContext.isAppConnected()){
                    progress!!.show()
                   mViewModel!!.createGroup(editTextGroupName.text.toString()).observe(this,
                       Observer {
                           progress!!.dismiss()
                           if(it!=null){
                               if(it.status_code==CODE_SUCCESS){
                                   mContext.showToastMsg(getString(R.string.msg_group_created),1)
                                    getGroups()
                               }else{
                                   mContext.responseHandler(it.status_code,it.message)
                               }
                           }
                       })
                }
            }
        }

        btnAddToGroup.setOnClickListener {
            findNavController().navigate(R.id.action_createGroupFragment_to_addGroupFragment)
            mViewModel!!.setStudentData(selectedStudentList)
            editTextGroupName.setText("")

        }
         checkBoxAll.setOnClickListener {
             if(checkBoxAll.isChecked){
                 isSelectAllChecked = true
                 setStudentAdapter()
                 if(studentList.size>0){
                     for(i in studentList){
                         val student = Student(i.first_name,i.id.toString(),isSelectAllChecked)
                         if(!selectedStudentList.contains(student)){
                             selectedStudentList.add(student)
                         }
                     }
                 }

             }else{
                 selectedStudentList.clear()
                 isSelectAllChecked = false
                 setStudentAdapter()
             }

         }

    }

    override fun onItemChecked(student: Student) {
        selectedStudentList.add(student)
    }

    override fun onItemUnchecked(student: Student) {
           selectedStudentList.remove(student)
    }

    override fun onResume() {
        super.onResume()
        checkBoxAll.isChecked = false
        isSelectAllChecked = false
        selectedStudentList.clear()
    }

    override fun onViewIemClick(pos: Int, id: String) {
        val action = CreateGroupFragmentDirections.actionCreateGroupFragmentToGroupMembersFragment(id)
       findNavController().navigate(action)
    }

    override fun onAddStu(id: String) {
        val action = CreateGroupFragmentDirections.actionCreateGroupFragmentToAddStudentFragment(id)
        findNavController().navigate(action)
    }

    override fun onDelete(pos: Int, id: String) {
        if(mContext.isAppConnected()){
            progress!!.show()
            mViewModel!!.deleteGroup(id).observe(viewLifecycleOwner, Observer {
                progress!!.dismiss()
                if(it.status_code== CODE_SUCCESS){
                    mContext.showToastMsg("Group Deleted Successfully.", ERROR)
                    mViewModel!!.getGroups()
                }else{
                    mContext.responseHandler(it.status_code,it.message)
                }
            })
        }

    }

}
