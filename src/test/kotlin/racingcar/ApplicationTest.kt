package racingcar

import camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest
import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ApplicationTest : NsTest() {
    @Test
    fun `전진 정지`() {
        assertRandomNumberInRangeTest(
            {
                run("pobi,woni", "1")
                assertThat(output())
                    .contains("pobi : -", "woni : ", "최종 우승자 : pobi")
                    .doesNotContain("woni : -")
            },
            MOVING_FORWARD, STOP
        )
    }

    @Test
    fun `정지 정지`() {
        assertRandomNumberInRangeTest(
            {
                run("pobi,woni", "1")
                assertThat(output())
                    .contains("pobi : ", "woni : ", "최종 우승자 : pobi, woni")
                    .doesNotContain("pobi : -", "woni : -")
            },
            STOP, STOP
        )
    }

    @Test
    fun `전진 전진`() {
        assertRandomNumberInRangeTest(
            {
                run("pobi,woni", "1")
                assertThat(output())
                    .contains("pobi : -", "woni : -", "최종 우승자 : pobi, woni")
            },
            MOVING_FORWARD, MOVING_FORWARD
        )
    }

    @Test
    fun `pobi만 2회 전진`() {
        assertRandomNumberInRangeTest(
            {
                run("pobi,woni", "2")
                assertThat(output())
                    .contains("pobi : -", "pobi : --", "woni : ", "최종 우승자 : pobi")
                    .doesNotContain("woni : -")
            },
            MOVING_FORWARD, STOP, MOVING_FORWARD, STOP
        )
    }

    @Test
    fun `jun만 2회 전진`() {
        assertRandomNumberInRangeTest(
            {
                run("pobi,woni,jun", "2")
                assertThat(output())
                    .contains("pobi : ", "woni : ", "jun : -", "jun : --", "최종 우승자 : jun")
                    .doesNotContain("pobi : -", "woni : -")
            },
            STOP, STOP, MOVING_FORWARD, STOP, STOP, MOVING_FORWARD
        )
    }

    @Test
    fun `이름에 대한 예외 처리`() {
        assertSimpleTest {
            assertThrows<IllegalArgumentException> { runException("pobi,javaji", "1") }
        }
    }

    @Test
    fun `입력이 없는 경우`() {
        assertSimpleTest {
            runException()
            assertThat(output()).isEqualTo("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        }
    }

    @ValueSource(strings = [" ", "  ", "   ", "\n", "\t"])
    @ParameterizedTest
    fun `첫 입력에서 공백을 입력한 경우에 대한 예외처리`(value: String) {
        assertSimpleTest {
            assertThrows<IllegalArgumentException>("경주할 자동차 이름들을 올바르게 입력해주세요.") { runException(value) }
            assertThat(output()).isEqualTo("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        }
    }

    @ValueSource(strings = ["javaji", "pobi,javaji"])
    @ParameterizedTest
    fun `첫 입력에서 이름에 대한 예외처리 - 5자리 초과`(value: String) {
        assertSimpleTest {
            assertThrows<IllegalArgumentException>("경주할 자동차 이름을 올바르게 입력해주세요. (1~5자) ==> javaji") { runException(value) }
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        }
    }

    @ValueSource(strings = [",", "pobi,"])
    @ParameterizedTest
    fun `첫 입력에서 이름에 대한 예외처리 - 공백`(value: String) {
        assertSimpleTest {
            assertThrows<IllegalArgumentException>("경주할 자동차 이름을 올바르게 입력해주세요. (1~5자) ==> ") { runException(value) }
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        }
    }

    @ValueSource(strings = ["pobi", "pobi,woni", "pobi, woni"])
    @ParameterizedTest
    fun `첫 입력만 한 경우에 대한 예외처리`(value: String) {
        assertSimpleTest {
            runException(value)
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)", "시도할 횟수는 몇 회인가요?")
        }
    }

    @ValueSource(strings = [" ", "  ", "   ", "\n", "\t"])
    @ParameterizedTest
    fun `두번째 입력이 공백인 것에 대한 예외처리`(value: String) {
        assertSimpleTest {
            runException("pobi", value)
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)", "시도할 횟수는 몇 회인가요?")
        }
    }

    @ValueSource(strings = ["1 2", "1a", "a1", "a", "가", "👍"])
    @ParameterizedTest
    fun `두번째 입력이 숫자가 아닌 것에 대한 예외처리`(value: String) {
        assertSimpleTest {
            assertThrows<IllegalArgumentException>("시도할 횟수는 10진수 숫자만 가능합니다.") { runException("pobi", value) }
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)", "시도할 횟수는 몇 회인가요?")
        }
    }

    @Test
    fun `자동차가 1개, 횟수가 0인 경우에 대한 예외처리`() {
        assertSimpleTest {
            assertThrows<IllegalArgumentException>("시도할 횟수는 1회 이상만 가능합니다.") { runException("pobi", "0") }
            assertThat(output()).contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)", "시도할 횟수는 몇 회인가요?")
        }
    }

    @Test
    fun `자동차가 1개, 횟수가 1번인 경우`() {
        assertSimpleTest {
            run("pobi", "1")
            assertThat(output())
                .contains("최종 우승자 : pobi")
                .contains("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)", "시도할 횟수는 몇 회인가요?")
        }
    }

    public override fun runMain() {
        main()
    }

    companion object {
        private const val MOVING_FORWARD = 4
        private const val STOP = 3
    }
}
