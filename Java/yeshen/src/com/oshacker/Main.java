package com.oshacker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void print(int index,Object object) {
        System.out.println(String.format("%d %s",index,object.toString()));
    }

    public static void testString() {
        String str="hello world";
        print(1,str.indexOf('e'));
        print(2,str.charAt(1));
        print(3,str.codePointAt(1));//e的ascii
        print(4,str.compareToIgnoreCase("HELLO WORLD"));//0
        print(5,str.compareTo("hello uorld"));//2
        print(6,str.compareTo("hello xorld"));//-1
        print(7,str.contains("hello"));
        print(8,str.toUpperCase());
        print(9,str.concat("!!!"));
        print(10,str.endsWith("ld"));
        print(11,str.startsWith("he"));
        print(12,str.replace('o','e'));
        print(13,str.replaceAll("l|w","v"));
        print(14,str.replaceAll("hel","ss"));
        print(15,str+str);//字符串是不可变的对象，每次这样都会产生新的对象

        //StringBuilder是线程不安全的
        StringBuilder sb=new StringBuilder();//不产生新的对象
        sb.append('i');
        sb.append("f");
        sb.append(1);
        print(16,sb.toString());

        print(17,String.valueOf(15));//object->String
    }

    public static void testControllFlow() {
        String grade="B";
        switch (grade) {//jdk1.8后开始支持字符串
            case "A":
                print(1,">80");
                break;
            case "B":
                print(2,"60-80");
                break;
            case "C":
                print(3,"<60");
                break;
            default:
                print(4,"未知");
                break;
        }
    }

    public static void testList() { //时间复杂度O(n)
        ArrayList<String> list=new ArrayList<>(10);
        for (int i=0;i<4;i++) {
            list.add(String.valueOf(i));
        }
        print(1,list);

        ArrayList<String> list1=new ArrayList<>();
        for (int i=0;i<4;i++) {
            list.add(String.valueOf(i*i));
        }
        list.addAll(list1);
        print(2,list);

        list.remove(1);
        print(3,list);

        list.remove(String.valueOf(3));
        print(4,list);

        print(5,list.get(1));

        Collections.reverse(list);
        print(6,list);

        Collections.sort(list);//默认从小到大
        print(7,list);

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
                //先比分数
                //再比学号
            }
        });
        print(8,list);

        for (String str:list) {
            print(9,str);
        }

        for(int i=0;i<list.size();i++) {
            print(10,list.get(i));
        }

        //普通数组
        int[] arr=new int[]{11,22,33};
        print(11,arr[2]);
    }

    public static void testHashMap() {
        Map<String,String> map=new HashMap<>();
        for (int i=0;i<4;i++) {
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        print(1,map);

        for (Map.Entry<String,String> entry: map.entrySet()) {
            print(2,entry.getKey()+"="+entry.getValue());
        }

        Set<String> set=map.keySet();
        for (String str:set) {
            print(3,str+"="+map.get(str));
        }

        print(4,map.keySet());
        print(5,map.values());
        map.replace("3","27");
        print(6,map.get("3"));
        print(7,map.containsKey("A"));
        print(8,map.remove("2"));
    }

    public static void testSet() {
        Set<String> set=new HashSet<>();
        for (int i=0;i<4;i++) {
            set.add(String.valueOf(i));
            set.add(String.valueOf(i));
        }
        print(1,set);//set中的元素不能重复

        String[] list=new String[]{"aa","bb","cc"};
        set.addAll(Arrays.asList(list));
        print(2,set);
    }

    public static void testException() {
        try {
            //int a=1/0;
            int k=0;
            if (k==0) {//业务异常
                throw new Exception("不允许等于0");
            }
        } catch (Exception e) {
            print(1,e.getMessage());
        } finally {
            print(2,"finally");
        }
    }

    //面向对象：封装、继承、多态
    public static void testObject() {
        Animal animal=new Animal("xiaohua",3);
        animal.say();

        Animal human=new Human("zhangsan",23,"China");
        human.say();
    }

    //随机数:伪随机（种子->函数->结果1->同一函数->结果2...）
    public static void testRandom() {
        Random random=new Random();//默认种子跟时间相关
        print(1,random.nextInt(100));//0-100

        random.setSeed(1);//种子固定，结果固定（应用：不同的种子就会有不同试题顺序的考卷
                                            //并且可以重现，一个种子产生的随机数是确定的 )
        print(2,random.nextInt(100));

        List<Integer> list=Arrays.asList(new Integer[]{1,2,3,4,5});
        Collections.shuffle(list);//洗牌
        print(3,list);
    }

    public static void testDate() {
        Date date=new Date();
        print(1,date);
        print(2,date.getTime());//距1970.01.01 00:00:00多少s

        DateFormat df=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        print(3,df.format(date));
    }

    public static void testOther() {
        print(1,UUID.randomUUID());

        print(2,Math.log(8));//自然对数
        print(3, Math.max(3,10));
        print(4, Math.min(3,9));
        print(5, Math.ceil(2.2));//进一位
        print(6, Math.floor(2.2));//抹零
    }

    public static void main(String[] args) {
        //testString();
        //testControllFlow();
        //testList();
        //testHashMap();
        //testSet();
        //testException();
        //testObject();
        //testRandom();
        testDate();
        //testOther();

    }
}
