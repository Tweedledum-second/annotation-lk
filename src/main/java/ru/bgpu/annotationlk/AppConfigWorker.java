package ru.bgpu.annotationlk;

import org.reflections.Reflections;
import org.reflections.scanners.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppConfigWorker {

    private static Logger logger = Logger.getLogger(AppConfigWorker.class.getName());
    
    public static Object convertStringArrayToPrimitiveArray(String[] stringArray, Class<?> targetType) {
        if (targetType == int.class) {
            return convertToIntArray(stringArray);
        } else if (targetType == double.class) {
            return convertToDoubleArray(stringArray);
        } else if (targetType == float.class) {
            return convertToFloatArray(stringArray);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый примитивный тип: " + targetType.getName());
        }
    }
    
    private static Integer[] convertToIntegerArray(String[] stringArray)
    {
        Integer[] integerArray = new Integer[stringArray.length];
        for (int i = 0; i < stringArray.length; i++)
        {
            integerArray[i] = Integer.decode(stringArray[i]);
        }
        return integerArray;
    }
    
    private static Float[] convertToFloatClassArray(String[] stringArray)
    {
        Float[] floatArray = new Float[stringArray.length];
        for (int i = 0; i < stringArray.length; i++)
        {
            floatArray[i] = Float.valueOf(stringArray[i]);
        }
        return floatArray;
    }
    
    private static Double[] convertToDoubleClassArray(String[] stringArray)
    {
        Double[] doubleArray = new Double[stringArray.length];
        for (int i = 0; i < stringArray.length; i++)
        {
            doubleArray[i] = Double.valueOf(stringArray[i]);
        }
        return doubleArray;
    }

    private static int[] convertToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.decode(stringArray[i]).intValue();
        }
        return intArray;
    }

    private static double[] convertToDoubleArray(String[] stringArray) {
        double[] doubleArray = new double[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }
        return doubleArray;
    }

    private static float[] convertToFloatArray(String[] stringArray) {
        float[] floatArray = new float[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            floatArray[i] = Float.parseFloat(stringArray[i]);
        }
        return floatArray;
    }
    
    public static Object parseField(Class<?> type, String value){
        
        if (value.startsWith("{") && value.endsWith("}")) {
            String[] stringArray = value.substring(1, value.length() - 1).split(",");
            if (type.equals(String[].class)) {
                return stringArray;
            } else if (type.equals(Integer[].class)) { 
                return convertToIntegerArray(stringArray);
            } else if (type.equals(Float[].class)) {
                return convertToFloatArray(stringArray);
            } else if (type.equals(Double.class)) {
                return convertToDoubleArray(stringArray);
            } else if (type.equals(int[].class)) {
                return convertToIntArray(stringArray);
            } else if (type.equals(double[].class)) {
                return convertToDoubleArray(stringArray);
            } else if (type.equals(float[].class)) {
                return convertToFloatArray(stringArray);
            }
        } else {
            if (!value.startsWith("{") && !value.endsWith("}")) {
                Object result;
                if (type.equals(String.class)) {
                    result = value;
                    return result;
                } else if (type.equals(Integer.class)) {
                    result = Integer.decode(value);
                    return result;
                } else if (type.equals(Float.class)) {
                    result = Float.valueOf(value);
                    return result;
                } else if (type.equals(Double.class)) {
                    result = Double.valueOf(value);
                    return result;
                } else if (type.equals(int.class)) {
                    result = Integer.decode(value).intValue();
                    return result;
                } else if (type.equals(float.class)) {
                    result = Float.valueOf(value).floatValue();
                    return result;
                } else if (type.equals(double.class)) {
                    result = Double.valueOf(value).doubleValue();
                    return result;
                }
            } else {
                
            }
        }
        return null;
    }

    public static void configProcessing(String prefix, String filePropName) {

        Reflections reflections = new Reflections(prefix, Scanners.FieldsAnnotated);

        File prop = new File(filePropName);
        if(prop.isFile()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(prop));

                reflections.getFieldsAnnotatedWith(AppConfig.class).forEach(
                        field -> {

                            String value = properties.getProperty(
                                    field.getName(),
                                    field.getAnnotation(AppConfig.class).defValue()
                            );
                            Object targetValue = parseField(field.getType(), value);
                            
                            try {
                                field.setAccessible(true);
                                field.set(field.getDeclaringClass(), targetValue);
                                field.setAccessible(false);
                            } catch (IllegalAccessException e) {
                                logger.log(
                                        Level.WARNING,
                                        "error set "+field.getDeclaringClass().getName()
                                                +"."+field.getName()+" "+value
                                );
                            }

//                            System.out.println(field.getName());
                        }
                );
            } catch (Exception e) {
                logger.log(Level.WARNING, "error load properties", e);
            }
        } else {
            logger.log(Level.WARNING, "config file not found");
        }
    }

}
