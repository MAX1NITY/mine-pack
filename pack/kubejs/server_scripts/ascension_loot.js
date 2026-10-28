// Wanderer Essence turns up in dungeon and structure chests, so exploring always
// feeds the Wanderer path without needing machines or spells.

ServerEvents.chestLootTables(event => {
  const chests = [
    'minecraft:chests/simple_dungeon',
    'minecraft:chests/abandoned_mineshaft',
    'minecraft:chests/stronghold_corridor',
    'minecraft:chests/desert_pyramid',
    'minecraft:chests/jungle_temple',
    'minecraft:chests/nether_bridge',
    'minecraft:chests/bastion_treasure',
    'minecraft:chests/end_city_treasure'
  ]
  chests.forEach(id => {
    event.modify(id, table => {
      table.addPool(pool => {
        pool.rolls = 1
        pool.addItem('ascension_core:wanderer_essence', 8)
        pool.addEmpty(92)
      })
    })
  })
})
