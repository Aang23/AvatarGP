/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.fml.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.entity.SpongeProfession;
import org.spongepowered.common.registry.type.entity.ProfessionRegistryModule;
import org.spongepowered.mod.interfaces.IMixinVillagerProfession;
import org.spongepowered.mod.registry.SpongeForgeVillagerRegistry;

@Mixin(value = VillagerRegistry.class, remap = false)
public class MixinVillagerRegistry {

    private static final String REGISTER_PROFESSION_ID = "register(Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerProfession;I)V";
    private static final String
            REGISTRY_REGISTER =
            "Lnet/minecraftforge/fml/common/registry/FMLControlledNamespacedRegistry;register(ILnet/minecraft/util/ResourceLocation;Lnet/minecraftforge/fml/common/registry/IForgeRegistryEntry;)V";

    @SuppressWarnings({"deprecation", "unchecked"})
    @Redirect(method = REGISTER_PROFESSION_ID, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/RegistryNamespaced;register(ILjava/lang/Object;Ljava/lang/Object;)V", remap = false), remap = false)
    private void registerForgeVillager(RegistryNamespaced registry, int id, Object key, Object value) {
        final VillagerRegistry.VillagerProfession villagerProfession = (VillagerRegistry.VillagerProfession) value;
        final RegistryNamespaced<ResourceLocation, VillagerRegistry.VillagerProfession> villagerRegistry = (RegistryNamespaced<ResourceLocation, VillagerRegistry.VillagerProfession>) registry;
        villagerRegistry.register(id, ((IMixinVillagerProfession) villagerProfession).getName(), villagerProfession);
        final int professionId = villagerRegistry.getIDForObject(villagerProfession);
        final IMixinVillagerProfession mixinProfession = (IMixinVillagerProfession) villagerProfession;
        final SpongeProfession spongeProfession = new SpongeProfession(professionId, mixinProfession.getId(), mixinProfession.getProfessionName());
        final SpongeProfession registeredProfession = SpongeForgeVillagerRegistry.validateProfession(villagerProfession, spongeProfession);
        ProfessionRegistryModule.getInstance().registerAdditionalCatalog(registeredProfession);
    }

}
