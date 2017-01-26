import DS from 'ember-data';

export default DS.Model.extend({
  username: DS.attr("string"),
  password: DS.attr("string"),
  host: DS.attr("string"),
  protocol: DS.attr("string"),
  port: DS.attr("number"),
  name: DS.attr("string")
});
