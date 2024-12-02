package com.ndmq.moneynote.data.repository

import com.tencent.mmkv.MMKV

class MMKVRepository(
    private val mmkv: MMKV
) {

    fun isFirstTime(): Boolean {
        val result = mmkv.decodeBool(IS_FIRST_TIME, false)
        mmkv.encode(IS_FIRST_TIME, true)
        return result
    }

    companion object {
        const val IS_FIRST_TIME = "IS_FIRST_TIME"
    }
}