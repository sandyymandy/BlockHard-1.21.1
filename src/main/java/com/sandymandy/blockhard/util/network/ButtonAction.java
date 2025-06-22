package com.sandymandy.blockhard.util.network;

import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public record ButtonAction(Text label, BiConsumer<AbstractGirlEntity, PlayerEntity> action) {}
