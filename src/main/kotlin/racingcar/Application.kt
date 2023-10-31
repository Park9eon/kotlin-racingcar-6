package racingcar

import camp.nextstep.edu.missionutils.Console.readLine

fun main() {
    println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")

    val input1: String? = readLine()

    if (input1.isNullOrBlank()) {
        throw IllegalArgumentException("경주할 자동차 이름들을 올바르게 입력해주세요.")
    }

    val cardNames = input1.split(",").map {
        it.trim()
    }.onEach {
        if (it.isEmpty() || it.length > 5) {
            throw IllegalArgumentException("경주할 자동차 이름을 올바르게 입력해주세요. (1~5자) ==> $it")
        }
    }

    if (cardNames.isEmpty()) {
        throw IllegalArgumentException("경주할 자동차 이름들을 올바르게 입력해주세요.")
    }

    println("시도할 횟수는 몇 회인가요?")

    val tryCount: String? = readLine()
}
