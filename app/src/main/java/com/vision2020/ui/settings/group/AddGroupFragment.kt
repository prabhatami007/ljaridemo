package com.vision2020.ui.settings.group
import BaseFragment
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.GroupMembersAdapter
import com.vision2020.adapter.SelectedStudentListAdapter
import com.vision2020.adapter.StudentListAdapter
import com.vision2020.data.GroupLeader
import com.vision2020.data.Student
import com.vision2020.data.response.GroupListing
import com.vision2020.data.response.GroupMembers
import com.vision2020.ui.views.dialog.GroupLeaderListDialog
import com.vision2020.ui.views.dialog.ItemCallBack
import com.vision2020.ui.views.dialog.OptionListDialog
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.AppConstant.SUCCESS
import com.vision2020.utils.isAppConnected
import com.vision2020.utils.progressDialog
import com.vision2020.utils.responseHandler
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_add_group.*
import kotlinx.android.synthetic.main.fragment_create_group.*
import java.lang.IndexOutOfBoundsException

class AddGroupFragment : BaseFragment<AddGroupViewModel>(), SelectedStudentListAdapter.OnItemClick {
    private var groupViewModel: GroupViewModel?=null
    private var studentList = arrayListOf<Student>()
    private var groupList = arrayListOf<GroupListing.Data>()
    private var membersList = arrayListOf<GroupMembers.Data>()
    private var userId = arrayListOf<String>()
    private var finalList = arrayListOf<GroupLeader>()
    private var groupId:String=""
    private var leaderId:String=""
    private lateinit var studentListAdapter : SelectedStudentListAdapter
    override val layoutId: Int
        get() = R.layout.fragment_add_group
    override val viewModel: AddGroupViewModel
        get() =  ViewModelProvider(requireActivity()).get(AddGroupViewModel::class.java)

    override fun onCreateStuff() {
        progress= mContext.progressDialog(getString(R.string.request))
        groupViewModel = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
        groupViewModel!!.getStudentData().observe(viewLifecycleOwner,Observer{
            studentList = it
            setStudentAdapter()
        })
        if(mContext.isAppConnected()){
            progress!!.show()
            groupViewModel!!.getGroups().observe(viewLifecycleOwner, Observer {
                progress!!.dismiss()
                if(it!= null){
                    if(it.status_code == CODE_SUCCESS && it.data!=null){
                        groupList = it.data!!

                    }else{
                        mContext.responseHandler(it.status_code,it.message)
                    }
                }
            })

            mViewModel!!.getGroupMembers().observe(viewLifecycleOwner, Observer {
                progress!!.dismiss()
                if(it!=null){
                    if(it.status_code== CODE_SUCCESS && it.data!=null){
                        membersList = it.data!!
                        setAdapter()
                    }else{
                        mContext.responseHandler(it.status_code,it.message)
                    }
                }
            })
        }
    }

    private fun setAdapter() {
        if(!membersList.isNullOrEmpty()){
           textAlready.visibility = View.VISIBLE
            rvMembers.layoutManager = LinearLayoutManager(mContext)
            rvMembers.adapter = GroupMembersAdapter(mContext,membersList)
             getFullList()
        }else{
            mContext.showToastMsg("No Members Exist in Selected Group.", ERROR)
        }
    }

    private fun setStudentAdapter() {
        if(!studentList.isNullOrEmpty()){
            txtSelectedStu.visibility = View.VISIBLE
            rvAddStudent.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
            studentListAdapter = SelectedStudentListAdapter(mContext,studentList,this)
            rvAddStudent.adapter = studentListAdapter
            for(student in studentList){
               userId.add(student.studentId)
            }
        }
    }

    override fun initListeners() {
        spGroupName.setOnClickListener {
         if(!groupList.isNullOrEmpty()){
             OptionListDialog(mContext,R.style.pullBottomFromTop,R.layout.dialog_options,
                 groupList,"Select Your Group",object:ItemCallBack.Callback{
                     override fun selected(pos: Int) {
                         spGroupName.setText(groupList[pos].group_name)
                         groupId = groupList[pos].id.toString()
                         progress!!.show()
                         mViewModel!!.groupMembersReq(groupId)
                     }

                 }).show()
         }else{
             mContext.showToastMsg("No Group Found.Please Create a Group First!",ERROR)
         }

        }

        spGroupLeader.setOnClickListener {
            if(spGroupName.text!!.trim().isEmpty()){
              mContext.showToastMsg("Please Select Group first.",ERROR)
            }else{
                GroupLeaderListDialog(mContext,R.style.pullBottomFromTop,R.layout.dialog_options,finalList,
                    "Select Group Leader",object:ItemCallBack.Callback{
                        override fun selected(pos: Int) {
                           spGroupLeader.setText(finalList[pos].StudentName)
                            leaderId = if(finalList[pos].leaderId.isEmpty()){
                                finalList[pos].studentId
                            }else{
                                finalList[pos].leaderId
                            }
                        }

                    }).show()
            }

        }

        btnAdd.setOnClickListener {
            if(spGroupName.text!!.trim().isEmpty()){
                mContext.showToastMsg(getString(R.string.error_group),ERROR)
            }else if(spGroupLeader.text!!.trim().isEmpty()){
                mContext.showToastMsg(getString(R.string.error_group_leader),ERROR)
            }else{
                if(mContext.isAppConnected()){
                    progress!!.show()
                  mViewModel!!.addToGroup(groupId,leaderId,userId).observe(viewLifecycleOwner,
                      Observer {
                         progress!!.dismiss()
                          if(it.status_code== CODE_SUCCESS){
                             mContext.showToastMsg(it.message, SUCCESS)
                              mViewModel!!.groupMembersReq(groupId)
                          }else{
                              mContext.responseHandler(it.status_code,it.message)
                          }
                      })
                }
            }
        }

    }

    private fun getFullList() {
        if(finalList.isNotEmpty()){
           finalList.clear()
        }
        for(student in studentList){
            val data = GroupLeader(student.StudentName,student.studentId,"")
            finalList.add(data)
        }
        for(member in membersList){
            val student = GroupLeader(member.first_name,member.id.toString(),member.leader_id.toString())
            finalList.add(student)
        }
    }
    override fun updateStatus(pos: Int) {
        studentList.removeAt(pos)
        studentListAdapter.notifyItemRemoved(pos)
        studentListAdapter.notifyItemRangeChanged(pos, studentList.size)
        if(studentList.isEmpty()){
            txtSelectedStu.visibility = View.GONE
        }
        getFullList()
    }

}
