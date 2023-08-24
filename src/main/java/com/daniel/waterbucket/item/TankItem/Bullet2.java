package com.daniel.waterbucket.item.TankItem;

public class Bullet2 extends Bullet{
    public Bullet2(Settings settings) {
        super(settings);
    }

    @Override
    public int setCount() {
        return super.setCount();
    }

    @Override
    public int setCoolDown() {
        return 120;
    }

    @Override
    public int setDamage() {
        return 5;
    }

    @Override
    public int setPower() {
        return 4;
    }
}
