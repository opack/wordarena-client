set OUTPUT_DIR="E:\Projets\Programmes\WordArena\wordarena-client\android\assets\textures"

:: Pack les images du répertoire general
java -cp lib/* com.badlogic.gdx.tools.texturepacker.TexturePacker general %OUTPUT_DIR% general >> log.txt

:: Pack les images du répertoire arena_skins
java -cp lib/* com.badlogic.gdx.tools.texturepacker.TexturePacker arena_skins\default %OUTPUT_DIR% arena_skin_default >> log.txt
