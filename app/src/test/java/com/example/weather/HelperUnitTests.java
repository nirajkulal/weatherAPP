package com.example.weather;

import com.example.weather.utils.Helper;

import org.junit.Assert;
import org.junit.Test;

public class HelperUnitTests {

    @Test
    public void testCelsiusConversion() {
        String expected = "36.85";
        String outPut = Helper.getCelsius(310.00);
        Assert.assertEquals(outPut, expected);
    }

    @Test
    public void testCelsiusConversionFailCase() {
        String expected = "37";
        String outPut = Helper.getCelsius(310.00);
        Assert.assertNotEquals(outPut, expected);
    }

    @Test
    public void testCelsiusIntConversion() {
        int expected = 36;
        int outPut = Helper.getCelsiusInt(310.00);
        Assert.assertEquals(outPut, expected);
    }

    @Test
    public void testEpochConversion() {
        String expected = "16 May 16:31";
        String outPut = Helper.epochToDate(1621162877052L);
        Assert.assertEquals(outPut, expected);
    }

    @Test
    public void testEpochConversionFailCase() {
        String expected = "16:31";
        String outPut = Helper.epochToDate(1621162877052L);
        Assert.assertNotEquals(outPut, expected);
    }

    @Test
    public void testEpochConversion2() {
        String expected = "16:31";
        String outPut = Helper.epochToDate2(1621162877L);
        Assert.assertEquals(outPut, expected);
    }

    @Test
    public void testEpochConversion2FailCase() {
        String expected = "16 May 16:31";
        String outPut = Helper.epochToDate2(1621162877L);
        Assert.assertNotEquals(outPut, expected);
    }

    @Test
    public void testGetLimitedDecimal() {
        String expected = "12.54";
        String outPut = Helper.getLimitedDecimal(12.544444);
        Assert.assertEquals(outPut, expected);
    }

    @Test
    public void testGetLimitedDecimalFailCase() {
        String expected = "12.544";
        String outPut = Helper.getLimitedDecimal(12.544444);
        Assert.assertNotEquals(outPut, expected);
    }


    @Test
    public void testGetIcon() {
        String expected = "http://openweathermap.org/img/w/03d.png";
        String output = Helper.getIcon("03d");
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testGetIconFailCase() {
        String expected = "http://openweathermap.org/img/w/03d";
        String output = Helper.getIcon("03d");
        Assert.assertNotEquals(expected, output);
    }

}
