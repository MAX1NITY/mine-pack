// Greets a player the first time they join so the three paths are discoverable.

PlayerEvents.loggedIn(event => {
  const player = event.player
  if (player.persistentData.getBoolean('ascension_welcomed')) return
  player.persistentData.putBoolean('ascension_welcomed', true)
  player.tell(Text.gold('Welcome to Mine Pack.'))
  player.tell(Text.gray('Three paths advance you: Tech, Arcane and Wanderer. Use essences to level them.'))
  player.tell(Text.gray('Type /ascension status any time to see your progress.'))
})
