package com.daniel.waterbucket.item.TankItem;

public class Bullet1 extends Bullet{
    public Bullet1(Settings settings) {
        super(settings);
    }

    @Override
    public int setCount() {
        return super.setCount();
    }

    @Override
    public int setCoolDown() {
        return 60;
    }

    @Override
    public int setDamage() {
        return 10;
    }

    @Override
    public int setPower() {
        return 2;
    }
}
