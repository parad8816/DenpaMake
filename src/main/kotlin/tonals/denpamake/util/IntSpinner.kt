package tonals.denpamake.util

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.math.BigInteger

class IntSpinner(var min: Int, var max: Int, initialValue: Int) : Spinner<BigInteger>() {
    private val minBig
        get() = min.toBigInteger()

    private val maxBig
        get() = max.toBigInteger()

    init {
        valueFactory = object : SpinnerValueFactory<BigInteger>() {
            init {
                value = initialValue.toBigInteger()
                converter = object : StringConverter<BigInteger>() {
                    override fun toString(`object`: BigInteger): String {
                        return `object`.toString()
                    }

                    override fun fromString(string: String): BigInteger {
                        val bigInt = string.toBigIntegerOrNull()
                        return bigInt?.coerceIn(minBig, maxBig) ?: BigInteger.ZERO
                    }
                }
            }

            override fun decrement(steps: Int) {
                val result = value - steps.toBigInteger()
                value = result.coerceIn(minBig, maxBig)
            }

            override fun increment(steps: Int) {
                val result = value + steps.toBigInteger()
                value = result.coerceIn(minBig, maxBig)
            }
        }

        editor.textFormatter = TextFormatter<String> { change ->
            // 整数値のみを受け取る
            val regex = Regex("-?\\d*")

            if (change.controlNewText.matches(regex)) {
                if (change.controlNewText == "-") {
                    change.text = (0).toString()
                }
                change
            }
            else {
                null
            }
        }

        editor.textProperty().addListener { _, _, newValue ->
            val convertedValue = valueFactory.converter.fromString(newValue)
            valueFactory.value = convertedValue
            editor.text = convertedValue.toString()
        }
    }
}