package org.jenkinsci.plugins.fasternodeprovision.FasterNodeProvisionGlobalConfig

def f = namespace(lib.FormTagLib)

f.section(title:_("Faster Node Provision")) {
    f.entry(title: _("Disabled"), field: "disabled") {
        f.checkbox()
    }
}
