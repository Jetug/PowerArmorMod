package com.jetug.power_armor_mod.common.foundation.block.entity;

import com.google.common.collect.Lists;
import com.jetug.power_armor_mod.common.foundation.block.CastingTable;
import com.jetug.power_armor_mod.common.foundation.container.menu.GemCuttingStationMenu;
import com.jetug.power_armor_mod.common.foundation.item.CastItem;
import com.jetug.power_armor_mod.common.network.PacketSender;
import com.jetug.power_armor_mod.common.network.actions.CastingStatusAction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.jetug.power_armor_mod.common.foundation.registery.BlockEntityRegistry.GEM_CUTTING_STATION_BLOCK_ENTITY;

public class GemCuttingStationBlockEntity extends BaseContainerBlockEntity implements MenuProvider, RecipeHolder, WorldlyContainer{
    public static final int COOKING_TOTAL_TIME = 100;
    public int SIZE = 4;
    private final ItemStackHandler itemHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;

    public final ContainerData dataAccess = new ContainerData() {
        public int get(int p_58431_) {
            switch(p_58431_) {
                case 0:
                    return GemCuttingStationBlockEntity.this.litTime;
                case 1:
                    return GemCuttingStationBlockEntity.this.litDuration;
                case 2:
                    return GemCuttingStationBlockEntity.this.cookingProgress;
                case 3:
                    return GemCuttingStationBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int p_58433_, int p_58434_) {
            switch(p_58433_) {
                case 0:
                    GemCuttingStationBlockEntity.this.litTime = p_58434_;
                    break;
                case 1:
                    GemCuttingStationBlockEntity.this.litDuration = p_58434_;
                    break;
                case 2:
                    GemCuttingStationBlockEntity.this.cookingProgress = p_58434_;
                    break;
                case 3:
                    GemCuttingStationBlockEntity.this.cookingTotalTime = p_58434_;
            }

        }

        public int getCount() {
            return 4;
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public GemCuttingStationBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(GEM_CUTTING_STATION_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Gem Cutting Station");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("Gem Cutting Station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new GemCuttingStationMenu(pContainerId, pInventory, this, dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return lazyItemHandler.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }

    LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(
            Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

//    @Override
//    public void invalidateCaps()  {
//        super.invalidateCaps();
//        lazyItemHandler.invalidate();
//    }

//    @Override
//    protected void saveAdditional(@NotNull CompoundTag tag) {
//        tag.put("inventory", itemHandler.serializeNBT());
//        super.saveAdditional(tag);
//    }
//
//    @Override
//    public void load(CompoundTag nbt) {
//        super.load(nbt);
//        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
//    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


//    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GemCuttingStationBlockEntity pBlockEntity) {
////        if(hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity)) {
////            craftItem(pBlockEntity);
////        }
//    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        CompoundTag compoundtag = pTag.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }
    }


    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(pPlayer.getLevel(), pPlayer.position());
        pPlayer.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                list.add(p_155023_);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((AbstractCookingRecipe)p_155023_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, pPopVec, i);
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(pTag, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag.putInt(p_187449_.toString(), p_187450_);
        });
        pTag.put("RecipesUsed", compoundtag);
    }

    private boolean canCast(){
        var ingrSlot = items.get(0);
        //var fuelSlot = items.get(1);
        var castSlot = items.get(3);
        var resultSlot = items.get(2);

        if(castSlot.isEmpty() || ingrSlot.isEmpty() || !resultSlot.isEmpty())  return false;

        var castItem = (CastItem)castSlot.getItem();
        var result = castItem.getResult();
        var resAmount = result.getMaterial().getCraftPerSlotForSlot(result.part);

        return ingrSlot.getCount() >= resAmount;
    }

    private CastItem getCastItem(){
        return (CastItem) items.get(3).getItem();
    }

    private void tick(Level pLevel, BlockPos pPos, BlockState pState){

//
//        var castSlot = items.get(3);
//        var ingrSlot = items.get(0);
//
//        if(castSlot.getItem() instanceof CastItem castItem){
//            var result = castItem.getResult();
//            var resAmount = result.getMaterial().getCraftPerSlotForSlot(result.part);
//
//            cookingProgress++;
//
//            if(ingrSlot.getCount() >= resAmount){
//                itemHandler.setStackInSlot(2, new ItemStack(result));
//                items.set(2, new ItemStack(result));
//            }
//
//        }else{
//            cookingProgress = 0;
//        }


        boolean flag = isLit();
        boolean flag1 = false;
        if (isLit()) {
            --litTime;
        }

        var castSlot = items.get(3);
        var ingrSlot = items.get(0);
        var fuelSlot = items.get(1);

        if (!fuelSlot.isEmpty() && canCast()) {
            if (!isLit()) {
                litTime = getBurnDuration(fuelSlot);
                litDuration = litTime;
                if (isLit()) {
                    flag1 = true;
                    if (fuelSlot.hasContainerItem())
                        items.set(1, fuelSlot.getContainerItem());
                    else
                    if (!fuelSlot.isEmpty()) {
                        fuelSlot.shrink(1);
                        if (fuelSlot.isEmpty()) {
                            items.set(1, fuelSlot.getContainerItem());
                        }
                    }
                }
            }

            if (isLit() ) {
                ++cookingProgress;
                if (cookingProgress == cookingTotalTime) {
                    cookingProgress = 0;
                    cookingTotalTime = COOKING_TOTAL_TIME;
                    flag1 = true;

                    var result = getCastItem().getResult();
                    ingrSlot.shrink(result.getIngredientCount());
                    itemHandler.setStackInSlot(2, new ItemStack(result));
                    items.set(2, new ItemStack(result));
                }
            } else {
                cookingProgress = 0;
            }
        } else if (!isLit() && cookingProgress > 0) {
            cookingProgress = Mth.clamp(cookingProgress - 2, 0, cookingTotalTime);
        }

        if (flag != isLit()) {
            flag1 = true;
            pState = pState.setValue(CastingTable.LIT, Boolean.valueOf(isLit()));
            pLevel.setBlock(pPos, pState, 3);
        }

        if (flag1) {
            setChanged(pLevel, pPos, pState);
        }

        PacketSender.doClientAction(new CastingStatusAction(dataAccess, getBlockPos()));
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GemCuttingStationBlockEntity pBlockEntity) {
        pBlockEntity.tick(pLevel, pPos, pState);

//        var itemstack = pBlockEntity.items.get(1);
//        if (pBlockEntity.isLit() || !itemstack.isEmpty() && !pBlockEntity.items.get(0).isEmpty()) {
//            Recipe<?> recipe = pLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING, pBlockEntity, pLevel).orElse(null);
//            int i = pBlockEntity.getMaxStackSize();
//            if (!pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, pBlockEntity.items, i)) {
//                pBlockEntity.litTime = pBlockEntity.getBurnDuration(itemstack);
//                pBlockEntity.litDuration = pBlockEntity.litTime;
//                if (pBlockEntity.isLit()) {
//                    flag1 = true;
//                    if (itemstack.hasContainerItem())
//                        pBlockEntity.items.set(1, itemstack.getContainerItem());
//                    else
//                    if (!itemstack.isEmpty()) {
//                        itemstack.shrink(1);
//                        if (itemstack.isEmpty()) {
//                            pBlockEntity.items.set(1, itemstack.getContainerItem());
//                        }
//                    }
//                }
//            }
//
//            if (pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, pBlockEntity.items, i)) {
//                ++pBlockEntity.cookingProgress;
//                if (pBlockEntity.cookingProgress == pBlockEntity.cookingTotalTime) {
//                    pBlockEntity.cookingProgress = 0;
//                    pBlockEntity.cookingTotalTime = getTotalCookTime(pLevel, RecipeType.BLASTING, pBlockEntity);
//                    flag1 = true;
//                }
//            } else {
//                pBlockEntity.cookingProgress = 0;
//            }
//        } else if (!pBlockEntity.isLit() && pBlockEntity.cookingProgress > 0) {
//            pBlockEntity.cookingProgress = Mth.clamp(pBlockEntity.cookingProgress - 2, 0, pBlockEntity.cookingTotalTime);
//        }
//
//        if (flag != pBlockEntity.isLit()) {
//            flag1 = true;
//            pState = pState.setValue(CastingTable.LIT, Boolean.valueOf(pBlockEntity.isLit()));
//            pLevel.setBlock(pPos, pState, 3);
//        }
//
//        if (flag1) {
//            setChanged(pLevel, pPos, pState);
//        }

    }

    private boolean canBurn(@Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pStacks, int pStackSize) {
        if (!pStacks.get(0).isEmpty() && pRecipe != null) {
            ItemStack itemstack = ((Recipe<WorldlyContainer>) pRecipe).assemble(this);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = pStacks.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= pStackSize && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(@Nullable Recipe<?> pRecipe, NonNullList<ItemStack> pStacks, int pStackSize) {
        if (pRecipe != null && this.canBurn(pRecipe, pStacks, pStackSize)) {
            ItemStack itemstack = pStacks.get(0);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) pRecipe).assemble(this);
            ItemStack itemstack2 = pStacks.get(2);
            if (itemstack2.isEmpty()) {
                pStacks.set(2, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !pStacks.get(1).isEmpty() && pStacks.get(1).is(Items.BUCKET)) {
                pStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            Item item = pFuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pFuel, RecipeType.BLASTING);
        }
    }

    private static int getTotalCookTime(Level pLevel, RecipeType<? extends AbstractCookingRecipe> pRecipeType, Container pContainer) {
        return pLevel.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)pRecipeType, pContainer, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    public static boolean isFuel(ItemStack pStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, null) > 0;
    }

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};

    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return pSide == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex, pItemStack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if (pDirection == Direction.DOWN && pIndex == 1) {
            return pStack.is(Items.WATER_BUCKET) || pStack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack removeItem(int pIndex, int pCount) {
        return ContainerHelper.removeItem(this.items, pIndex, pCount);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int pIndex, ItemStack pStack) {
        ItemStack itemstack = this.items.get(pIndex);
        boolean flag = !pStack.isEmpty() && pStack.sameItem(itemstack) && ItemStack.tagMatches(pStack, itemstack);
        this.items.set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        if (pIndex == 0 && !flag) {
            this.cookingTotalTime = getTotalCookTime(this.level, RecipeType.BLASTING, this);
            this.cookingProgress = 0;
            this.setChanged();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (pIndex == 2) {
            return false;
        } else if (pIndex != 1) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(1);
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, RecipeType.BLASTING) > 0 || pStack.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    public void fillStackedContents(StackedContents pHelper) {
        for(ItemStack itemstack : this.items) {
            pHelper.accountStack(itemstack);
        }
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public void setRecipeUsed(@javax.annotation.Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }

    @javax.annotation.Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }
}