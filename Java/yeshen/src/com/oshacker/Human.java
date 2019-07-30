package com.oshacker;

public class Human extends Animal {//继承
    private String country;

    public Human(String name,int age,String Country) {
        super(name,age);//先构造父类
        this.country=country;
    }

    @Override
    public void say() {//Human类中的say与Animal中的say方法形成多态
        System.out.println("human have feeling");
    }
}
