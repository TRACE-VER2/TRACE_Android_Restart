package com.trace.myapplication.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import com.trace.myapplication.R
import com.trace.myapplication.Server.RequestToServer
import com.trace.myapplication.databinding.FragmentMainBinding
import com.trace.myapplication.main.dataType.ResponseMainList
import com.trace.myapplication.main.mainRecyclerview.MainListAdapter
import com.trace.myapplication.main.mainRecyclerview.MainListData
import com.trace.myapplication.startpage.myjwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    lateinit var mainlistAdapter: MainListAdapter
    val requestToServer=RequestToServer

    var datas = mutableListOf<MainListData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        //return inflater.inflate(R.layout.fragment_main, container, false)

        var btnToList= mutableListOf<ImageView>(binding.btnMaindoor,binding.btnDaehakro,binding.btnHansung,binding.btnIrongate,binding.btnSidedoor)
        var doorString= mutableListOf<String>("JUNGMOON","DAEMYUNG","HANSUNGSHIN","CHULMOON","JJOKMOON")
        var doorStringKo= mutableListOf<String>("정문/로터리","대명/대학로","한성/성신","철문","쪽문")
        for (i in 0..4){
            btnToList[i].setOnClickListener {
                //view.findNavController().navigate(R.id.action_mainFragment_to_listFragment)
                val action= MainFragmentDirections.actionMainFragmentToListFragment(doorString[i].toString(),doorStringKo[i].toString())
                view.findNavController().navigate(action)
            }
        }


        binding.mainFloating.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_editReview1Fragment)
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainlistAdapter= MainListAdapter(view.context)
        binding.rvMainList.adapter=mainlistAdapter
        loadDatas()

    }

    private fun loadDatas(){
        var tmpjwt:String= "Bearer "+myjwt
        var tmpaddress: String
        var tmpstar=4
        Log.d("loadDatas", "함수진입+ ${tmpjwt}")
        datas = mutableListOf<MainListData>()

        requestToServer.service.mainListRequest(
                "${tmpjwt}"
        ).enqueue(object : Callback<ResponseMainList> {
            override fun onFailure(call: Call<ResponseMainList>, t: Throwable) {
                Log.d("통신 실패", "${t.message }")
            }

            override fun onResponse(
                    call: Call<ResponseMainList>,
                    response: Response<ResponseMainList>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        Log.d("성공", response.body()!!.data.toString())
                        for (i in 0 until response.body()!!.data!!.content.size){
                            tmpaddress=response.body()!!.data!!.content[i].address
                            tmpaddress += response.body()!!.data!!.content[i].lotNumber

                            datas.apply{
                                add(MainListData(
                                        address = tmpaddress,
                                        star = tmpstar
                                ))
                            }

                        }
                        mainlistAdapter.datas=datas
                        mainlistAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("실패", "실패")
                    }
                }
            }
        })


    }

}

//        button_for_test_tolist.setOnClickListener {
//
//            //addextra보내기
//            val action=
//                MainFragmentDirections.actionMainFragmentToListFragment(
//                    "Hello ListFragment"
//                )
//            view.findNavController().navigate(action)
//
//        }
//
//        button_for_test_toreview.setOnClickListener {
//            view.findNavController().navigate(R.id.action_mainFragment_to_addReviewFragment)
//        }