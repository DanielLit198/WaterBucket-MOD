package com.daniel.waterbucket.item.TankItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class tank extends Item implements IAnimatable {
    public tank() {
        super(new Settings().group(ItemGroup.MISC));
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return GeckoLibUtil.createFactory(this);
    }
}
