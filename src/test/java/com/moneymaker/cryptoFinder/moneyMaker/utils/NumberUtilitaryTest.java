package com.moneymaker.cryptoFinder.moneyMaker.utils;

import net.bytebuddy.implementation.bind.annotation.DefaultMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilitaryTest {


    @ParameterizedTest
    @MethodSource("provideValidValues")
    public void shouldComputePercentageDifference(Double d1, Double d2, Double percentageDiff) {
        double actualDiff = NumberUtilitary.computePercentageDifference(d1, d2);
        assertEquals(percentageDiff, actualDiff);

    }

    private static Stream<Arguments> provideValidValues() {
        return Stream.of(
                Arguments.of(20d, 25d, 25d),
                Arguments.of(20d, 40d, 100d),
                Arguments.of(20d,80d,300d)
        );
    }
}