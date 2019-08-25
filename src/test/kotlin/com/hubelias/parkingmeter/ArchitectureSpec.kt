package com.hubelias.parkingmeter

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.Test


class ArchitectureSpec {
    private val sources by lazy {
        ClassFileImporter().importPackages("com.hubelias.parkingmeter")
    }

    @Test
    fun `domain does not depend on application and infrastructure`() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..")
                .check(sources)
    }

    @Test
    fun `application does not depend on infrastructure`() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
                .check(sources)
    }

    @Test
    fun `driver should not depend on occupation nor receipt`() {
        noClasses()
                .that().resideInAPackage("..domain.driver..")
                .should().dependOnClassesThat().resideInAnyPackage("..domain.occupation..", "..domain.receipt..")
                .check(sources)
    }

    @Test
    fun `receipt should not depend on occupation`() {
        noClasses()
                .that().resideInAPackage("..domain.receipt..")
                .should().dependOnClassesThat().resideInAnyPackage("..domain.occupation..")
                .check(sources)
    }
}