package com.daniel.waterbucket.init;

import com.daniel.waterbucket.Waterbucket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundsInit {
    public static SoundEvent Group_Water_Explosion = initSoundEvent("ground");


    public static SoundEvent initSoundEvent(String name){
        Identifier id =new Identifier(Waterbucket.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT,id,SoundEvent.of(id));
    }
    public static void init(){

    }

}
