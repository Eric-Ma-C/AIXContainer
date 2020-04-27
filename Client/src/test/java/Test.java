import java.util.*;

/**
 * @Date: 2020/4/20 21:13
 * @Author: EricMa
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        int a=0,b=3;
        Integer c=add(a,b);
        HashMap<String,Integer> hashMap=new HashMap<>(13);
        hashMap.put("dsa",32);
//        System.out.println(new Byte(c));
//        int[] li=new int[]{1,2,3};
//要转换的list集合
//        List<Integer> testList = new ArrayList<Integer>(){{add(1);add(2);add(3);}};
//        testList.get
//        //使用toArray(T[] a)方法
//        Integer[] array2 = testList.toArray(new Integer[testList.size()]);

//        Collections.addAll(list,li);
////        String[] arrays = new String[]{"aa","bb","cc"};
////        List<String> list = new ArrayList<>(Arrays.asList(arrays));
//        list.add(4);



    }

    private static int add(int a, int b) {
        return a+b;
    }

}

