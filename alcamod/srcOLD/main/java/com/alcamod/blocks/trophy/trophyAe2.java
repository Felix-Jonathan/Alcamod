import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.sounds.SoundType;

public class TrophyAe2 extends Block {
    public TrophyAe2() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(1.5f, 6.0f) // Définissez la dureté et la résistance à l'explosion.
                .sound(SoundType.STONE) // Son lorsque le bloc est placé, cassé, ou frappé.
                .lightLevel(blockState -> 15)); // Émission de lumière maximale.
    }
}
