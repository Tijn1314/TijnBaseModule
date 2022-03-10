package com.tijn.netlibrary.exception


class ResultException(var errCode: String?, var msg: String?) : Exception(msg)
