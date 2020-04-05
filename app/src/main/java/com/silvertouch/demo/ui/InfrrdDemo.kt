package com.silvertouch.demo.ui

import java.util.*

/*

* Test Case 1
* Loot Level = [3, 6, 2, 7, 5]
* Days == 2
*
*
* Test Case 2
* Loot Level = [7, 6, 5, 8, 4, 7, 10, 9]
* Days == 2
*
* */

var shipDayCount = 0

fun main() {

    val read = Scanner(System.`in`)

    println("Please Enter No. of Ships: ")
    val noOfShips = read.nextInt()

    val lootLevels = IntArray(noOfShips)
    println("Please Enter Loot Values for each Ships : ")

    for (i in 0 until noOfShips) {
        lootLevels[i] = read.nextInt()
    }

    shipDayCount = getShipDayCount(lootLevels)
    println("Number of days after which no ships are destroyed is : $shipDayCount")
}

private fun getShipDayCount(lootLevels: IntArray): Int {

    val shipRemoveList = Stack<Int>()

    for (i in lootLevels.size - 1 downTo 0) {

        if (i == 0) {
            break
        }

        if (lootLevels[i - 1] > lootLevels[i]) {
            shipRemoveList.push(i - 1)
        }
    }

    // check for empty stack
    if (shipRemoveList.isNotEmpty()) {

        val newLootLevels = arrayListOf<Int>()

        for (i in lootLevels.indices) {

            if (shipRemoveList.isNotEmpty() && i == shipRemoveList.peek()) {
                shipRemoveList.pop()
            } else {
                newLootLevels.add(lootLevels[i])
            }
        }

        println("Loot Level : $newLootLevels")

        // increment ship day count by 1
        shipDayCount = shipDayCount.plus(1)

        // call function again
        getShipDayCount(newLootLevels.toIntArray())
    }

    return shipDayCount
}
