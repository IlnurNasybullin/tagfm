package test.io.github.ilnurnasybullin.bool.expression.term;

import io.github.ilnurnasybullin.bool.expression.terms.Operator;
import io.github.ilnurnasybullin.bool.expression.terms.OperatorElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Ilnur Nasybullin
 */
public class OperatorElementTest {

    public static Stream<Arguments> _testPriorityThan_NotNullObjects_Successful_DataSet() {
        OperatorElement not0 = new OperatorElement(Operator.NOT, 0, 0);
        OperatorElement and0 = new OperatorElement(Operator.AND, 0, 1);
        OperatorElement or0 = new OperatorElement(Operator.OR, 0, 2);

        OperatorElement not1 = new OperatorElement(Operator.NOT, 1, 3);
        OperatorElement and1 = new OperatorElement(Operator.AND, 1, 4);
        OperatorElement or1 = new OperatorElement(Operator.OR, 1, 5);

        OperatorElement[] operators = {not0, and0, or0, not1, and1, or1};

        OperatorElement not01 = new OperatorElement(Operator.NOT, 0, 1);
        OperatorElement and02 = new OperatorElement(Operator.AND, 0, 2);
        OperatorElement or03 = new OperatorElement(Operator.OR, 0, 3);

        OperatorElement not14 = new OperatorElement(Operator.NOT, 1, 4);
        OperatorElement and15 = new OperatorElement(Operator.AND, 1, 5);
        OperatorElement or16 = new OperatorElement(Operator.OR, 1, 6);

        OperatorElement[] notEqualOperators = new OperatorElement[]{not01, and02, or03, not14, and15, or16};

        Arguments[] arguments = new Arguments[6 * 6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                OperatorElement operator = i == j ? notEqualOperators[j] : operators[j];
                arguments[i * 6 + j] = Arguments.of(operators[i], operator, i <= j);
            }
        }

        return Arrays.stream(arguments);
    }

    @MethodSource("_testPriorityThan_NotNullObjects_Successful_DataSet")
    @ParameterizedTest
    public void testPriorityThan_NotNullObjects_Successful(OperatorElement operator1,
                                                           OperatorElement operator2,
                                                           boolean expectedResult) {
        Assertions.assertEquals(expectedResult, operator1.priorityThan(operator2));
    }

}
