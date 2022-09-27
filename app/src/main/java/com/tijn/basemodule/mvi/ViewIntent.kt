package com.tijn.basemodule.mvi

/**
 *密封(Sealed)类是一个限制类层次结构的类。
 *可以在类名之前使用sealed关键字将类声明为密封类。
 *它用于表示受限制的类层次结构。
 *当对象具有来自有限集的类型之一，但不能具有任何其他类型时，使用密封类。
 *密封类的构造函数在默认情况下是私有的，它也不能允许声明为非私有。
 */
sealed class ViewIntent {
    object FetchUser : ViewIntent()
}