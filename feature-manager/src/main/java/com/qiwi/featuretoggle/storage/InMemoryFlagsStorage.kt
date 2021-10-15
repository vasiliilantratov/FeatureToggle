/**
 * Copyright (c) 2021 QIWI
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.qiwi.featuretoggle.storage

/**
 * An [ActualFlagsStorage] default implementation that stores actual flags and used flags cache in memory using [Map].
 */
internal class InMemoryFlagsStorage: ActualFlagsStorage {

    private val actualFlags = mutableMapOf<String, Any>()
    private val usedFlags = mutableMapOf<String, Any?>()

    override fun getFlag(key: String): Any? = synchronized(this) {
        return if(usedFlags.containsKey(key)) {
            usedFlags[key]
        }
        else {
            actualFlags[key].also {
                usedFlags[key] = it
            }
        }
    }

    override fun updateFlags(flags: Map<String, Any>, force: Boolean) = synchronized(this) {
        actualFlags.putAll(flags)
        if(force){
            usedFlags.clear()
        }
    }

    override fun resetUsedFlags() = synchronized(this){
        usedFlags.clear()
    }

    override fun resetAllFlags() = synchronized(this){
        actualFlags.clear()
        usedFlags.clear()
    }
}