package com.skydoves.pokedex.compose.features.notinteresting;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;

public class DexUtil {
  static void setDexClassLoaderElements(BaseDexClassLoader classLoader, Object elements) throws Exception {
    Class<BaseDexClassLoader> dexClassLoaderClass = BaseDexClassLoader.class;
    Field pathListField = dexClassLoaderClass.getDeclaredField("pathList");
    pathListField.setAccessible(true);
    Object pathList = pathListField.get(classLoader);
    Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
    dexElementsField.setAccessible(true);
    dexElementsField.set(pathList, elements);
  }

  static Object getDexClassLoaderElements(BaseDexClassLoader classLoader) throws Exception {
    Class<BaseDexClassLoader> dexClassLoaderClass = BaseDexClassLoader.class;
    Field pathListField = dexClassLoaderClass.getDeclaredField("pathList");
    pathListField.setAccessible(true);
    Object pathList = pathListField.get(classLoader);
    Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
    dexElementsField.setAccessible(true);
    Object dexElements = dexElementsField.get(pathList);
    return dexElements;
  }

  static Object joinArrays(Object o1, Object o2) {
    Class<?> o1Type = o1.getClass().getComponentType();
    Class<?> o2Type = o2.getClass().getComponentType();

    if(o1Type != o2Type)
      throw new IllegalArgumentException();

    int o1Size = Array.getLength(o1);
    int o2Size = Array.getLength(o2);
    Object array = Array.newInstance(o1Type, o1Size + o2Size);

    int offset = 0, i;
    for(i = 0; i < o1Size; i++, offset++)
      Array.set(array, offset, Array.get(o1, i));
    for(i = 0; i < o2Size; i++, offset++)
      Array.set(array, offset, Array.get(o2, i));

    return array;
  }
}
