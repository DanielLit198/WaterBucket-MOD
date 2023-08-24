package com.daniel.waterbucket.item.TankItem;

public class Bullet3 extends Bullet{
    public Bullet3(Settings settings) {
        super(settings);
    }

    @Override
    public int setCount() {
        return 3;
    }

    @Override
    public int setCoolDown() {
        return 120;
    }

    @Override
    public int setDamage() {
        return 15;
    }

    @Override
    public int setPower() {
        return 2;
    }
}
