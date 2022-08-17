package com.example.androidcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidcalculator.databinding.FragmentCalculatorBinding
import com.google.android.material.snackbar.Snackbar


class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding: FragmentCalculatorBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentCalculatorBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonZero.setOnClickListener { binding.textExpression.append("0") }

        binding.buttonOne.setOnClickListener { binding.textExpression.append("1") }

        binding.buttonTwo.setOnClickListener { binding.textExpression.append("2") }

        binding.buttonThree.setOnClickListener { binding.textExpression.append("3") }

        binding.buttonFour.setOnClickListener { binding.textExpression.append("4") }

        binding.buttonFive.setOnClickListener { binding.textExpression.append("5") }

        binding.buttonSix.setOnClickListener { binding.textExpression.append("6") }

        binding.buttonSeven.setOnClickListener { binding.textExpression.append("7") }

        binding.buttonEight.setOnClickListener { binding.textExpression.append("8") }

        binding.buttonNine.setOnClickListener { binding.textExpression.append("9") }

        binding.buttonDot.setOnClickListener { binding.textExpression.append(".") }

        binding.buttonOpenBracket.setOnClickListener { binding.textExpression.append("(") }

        binding.buttonCloseBracket.setOnClickListener { binding.textExpression.append(")") }

        binding.buttonDivide.setOnClickListener { binding.textExpression.append("/") }

        binding.buttonMultiply.setOnClickListener { binding.textExpression.append("*") }

        binding.buttonPlus.setOnClickListener { binding.textExpression.append("+") }

        binding.buttonMinus.setOnClickListener { binding.textExpression.append("-") }

        binding.buttonClear.setOnClickListener {
            binding.textExpression.text = ""
            binding.textResult.text = ""
        }

        binding.buttonBack.setOnClickListener {
            val currentExpression = binding.textExpression.text.toString()
            if (currentExpression.isNotEmpty()) {
                binding.textExpression.text =
                    currentExpression.substring(0, currentExpression.length - 1)
                binding.textResult.text = ""
            }
        }

        binding.buttonEquals.setOnClickListener {
            val expression = binding.textExpression.text.toString()
            val answer = Calculator().calculate(expression)
            val result: String = (if (answer.result - answer.result.toInt() == 0.0) {
                answer.result.toInt()
            } else {
                answer.result
            }).toString()

            if (result == "Infinity") {
                answer.error = "Вы ввели неверное математическое выражение, попробуйте еще раз"
            }
            if (answer.error == "") {
                binding.textResult.text = result
            } else {
                Snackbar.make(view, answer.error, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK") { }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

