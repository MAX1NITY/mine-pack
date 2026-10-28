// Ascension Core: essence recipes. Each path pulls from a different part of the pack,
// so you cannot advance on one playstyle alone forever.
//   Tech     -> Create + Mekanism
//   Arcane   -> Ars Nouveau + Iron's Spells + Botania
//   Wanderer -> exploration items (also found in dungeon chests, see loot script)

ServerEvents.recipes(event => {
  // Remove the placeholder vanilla recipes that ship with the mod.
  ;['tech', 'arcane', 'wanderer'].forEach(path => {
    event.remove({ output: `ascension_core:${path}_essence` })
  })

  // Tech Essence
  event.shaped('2x ascension_core:tech_essence', [
    ' P ',
    'BSB',
    ' P '
  ], {
    P: 'create:precision_mechanism',
    B: 'create:brass_ingot',
    S: 'mekanism:ingot_steel'
  })

  // Arcane Essence (two routes: Ars Nouveau + Iron's Spells, or Botania)
  event.shaped('2x ascension_core:arcane_essence', [
    ' S ',
    'AEA',
    ' S '
  ], {
    S: 'ars_nouveau:source_gem',
    A: 'minecraft:amethyst_shard',
    E: 'irons_spellbooks:arcane_essence'
  })
  event.shapeless('ascension_core:arcane_essence', [
    'botania:mana_diamond',
    'minecraft:amethyst_shard'
  ])

  // Wanderer Essence
  event.shaped('2x ascension_core:wanderer_essence', [
    ' E ',
    'CPC',
    ' E '
  ], {
    E: 'minecraft:emerald',
    C: 'minecraft:compass',
    P: 'minecraft:ender_pearl'
  })
})
