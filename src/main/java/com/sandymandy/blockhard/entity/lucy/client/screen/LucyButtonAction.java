package com.sandymandy.blockhard.entity.lucy.client.screen;

import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import com.sandymandy.blockhard.entity.lucy.LucyEntity;

import java.util.function.BiConsumer;

public record LucyButtonAction(Text label, BiConsumer<LucyEntity, PlayerEntity> action) {}
