const { spinner } = require('@clack/prompts');

/**
 * Creates a clack spinner instance.
 * Usage: const s = createSpinner(); s.start('Loading...'); s.stop('Done');
 */
function createSpinner() {
  return spinner();
}

module.exports = { createSpinner };
