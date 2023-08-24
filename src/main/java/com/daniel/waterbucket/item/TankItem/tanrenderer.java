package com.daniel.waterbucket.item.TankItem;

import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class tanrenderer extends GeoItemRenderer<tank> {
    public tanrenderer() {
        super(new tankModel());
    }
}
