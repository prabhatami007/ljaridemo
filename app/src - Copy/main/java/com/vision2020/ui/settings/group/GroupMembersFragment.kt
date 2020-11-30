package com.vision2020.ui.settings.group
import BaseFragment
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.ViewMembersAdapter
import com.vision2020.data.response.GroupMembers
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import kotlinx.android.synthetic.main.fragment_group_members.*
class GroupMembersFragment : BaseFragment<AddGroupViewModel>(),ViewMembersAdapter.OnItemClick {
    private var isCheckedAll = false
    private var list = arrayListOf<GroupMembers.Data>()
    private var stuList = arrayListOf<String>()
    val args : GroupMembersFragmentArgs by navArgs()
    override val layoutId: Int
        get() = R.layout.fragment_group_members
    override val viewModel: AddGroupViewModel
        get() = ViewModelProvider(this).get(AddGroupViewModel::class.java)

    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
      if(mContext.isAppConnected()){
          getStudent()
       }

        mViewModel!!.getGroupMembers().observe(viewLifecycleOwner, Observer {
            progress!!.dismiss()
            if(it!=null){
                if(it.status_code== CODE_SUCCESS && it.data!=null){
                    list = it.data!!
                    setAdapter()
                }else{
                    mContext.responseHandler(it.status_code,it.message)
                }
            }else{
                mContext.showToastMsg(getString(R.string.server_error),ERROR)
            }
        })


    }

    private fun getStudent() {
        progress!!.show()
        mViewModel!!.groupMembersReq(args.id)
    }

    private fun setAdapter() {
        rvViewGroupMembers.layoutManager = LinearLayoutManager(mContext)
        rvViewGroupMembers.adapter = ViewMembersAdapter(mContext,list,isCheckedAll,this)

    }

    override fun initListeners() {
        checkBoxAll.setOnClickListener {
            if(checkBoxAll.isChecked){
                isCheckedAll = true
                setAdapter()
                stuList.clear()
                if(list.size>0){
                    for(i in list){
                        stuList.add(i.student_id.toString())
                    }
                }

            }else{
                isCheckedAll = false
                setAdapter()
                 stuList.clear()
            }
        }
        btnDeleteGroup.setOnClickListener {
            if(stuList.isEmpty()){
                mContext.showToastMsg(getString(R.string.selected_no_member), ERROR)
            }else{
                if(mContext.isAppConnected()){
                    reqDeleteMember(stuList.joinToString(","))
                }
            }
        }

        btnAddStudent.setOnClickListener {
            val action = GroupMembersFragmentDirections.actionGroupMembersFragmentToAddStudentFragment(args.id)
            findNavController().navigate(action)
        }
    }

    override fun getItemCheck(pos:String) {
      stuList.add(pos)
    }

    override fun getItemUnCheck(student: String) {
        stuList.remove(student)
    }

    override fun deleteMember(memId: String) {
        if(mContext.isAppConnected()){
            reqDeleteMember(memId)
        }

    }

    private fun reqDeleteMember(id: String) {
        progress!!.show()
        mViewModel!!.deleteMembers(args.id,id).observe(viewLifecycleOwner, Observer {
            progress!!.dismiss()
            if(it.status_code== CODE_SUCCESS){
               mContext.showToastMsg("Deleted Successfully.", ERROR)
                if(isCheckedAll){
                    requireActivity().onBackPressed()
                }else{
                    mViewModel!!.groupMembersReq(args.id)
                }

            }else{
                mContext.responseHandler(it.status_code,it.message)
            }

        })
    }

}
