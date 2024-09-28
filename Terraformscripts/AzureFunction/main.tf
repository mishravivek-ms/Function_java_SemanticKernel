terraform {
    required_providers {
        azurerm = {
        source  = "hashicorp/azurerm"
        version = "~>2.31.1"
        }
    }
}
provider "azurerm" {
    features {}
}
 
resource "azurerm_resource_group" "rg" {
    name     = "optum_codewithai"
    location = "eastus"
}
 
resource "azurerm_storage_account" "sa" {
    name                     = "optumstorageacct"
    resource_group_name      = azurerm_resource_group.rg.name
    location                 = azurerm_resource_group.rg.location
    account_tier             = "Standard"
    account_replication_type = "LRS"
}
 
resource "azurerm_app_service_plan" "asp" {
    name                = "optum_app_service_plan"
    location            = azurerm_resource_group.rg.location
    resource_group_name = azurerm_resource_group.rg.name
    kind                = "FunctionApp"
    reserved            = true
    sku {
        tier = "Dynamic"
        size = "Y1"
    }
}
 
resource "azurerm_function_app" "functionapp" {
    name                       = "optumnurseaudit"
    location                   = azurerm_resource_group.rg.location
    resource_group_name        = azurerm_resource_group.rg.name
    app_service_plan_id        = azurerm_app_service_plan.asp.id
    storage_account_name       = azurerm_storage_account.sa.name
    storage_account_access_key = azurerm_storage_account.sa.primary_access_key
    os_type                    = "linux"
 
    version                    = "~4"
    site_config {
        linux_fx_version = "python|3.11"
    }
    app_settings = {
        FUNCTIONS_WORKER_RUNTIME = "python"
    }
}