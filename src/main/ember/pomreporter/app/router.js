import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('credentials', function() {
    this.route('new');
    this.route('list');
    this.route('edit', { path: '/edit/:id'});
  });
});

export default Router;
