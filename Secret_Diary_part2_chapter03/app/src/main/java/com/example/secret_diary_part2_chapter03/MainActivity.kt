package com.example.secret_diary_part2_chapter03

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            // 비밀번호 변경 중일 때 예외처리
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // sp를 다른 어플과 공유하지 않고 우리 어플에서만 사용할 것이므로 MODE_PRIVATE
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 패스워드 성공
                // TODO 다이어리 페이지 작성 후에 넘겨주어야 함
                startActivity(Intent(this, DiaryActivity::class.java))

            } else {
                // 패스워드 실패
                showErrorAlertDialog()
            }

            changePasswordButton.setOnClickListener {

                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

                if (changePasswordMode) {
                    // 번호 저장
//                  commit : UI thread를 잠깐 block 하고 실제로 데이터가 다 저장될 때까지 기다리는 것
//                  apply : 기다리지 않고 바로 비동기적으로 저장
//                  UI thread 에서는 무거운 작업을 하지 않는 것이 좋음 -> 네트워크 작업은 다른 thread에서 할 것
                    passwordPreferences.edit {
                        putString("password", passwordFromUser)
                        commit()
                    }

//                    passwordPreferences.edit(true) {
//                        putString("password", passwordFromUser)
//                    }

                    changePasswordMode = false
                    changePasswordButton.setBackgroundColor(Color.WHITE)

                } else {
                    // changePasswordMode 활성화 : 비밀번호가 맞는지 체크


                    if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                        changePasswordMode = true
                        Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                        changePasswordButton.setBackgroundColor(Color.RED)

                    } else {
                        // 패스워드 실패
                        showErrorAlertDialog()
                    }
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
//                        dialog와 which가 딱히 사용되지 않고 있으므로 J{dialog, which -> }를 {_, _ -> }로 표현
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}