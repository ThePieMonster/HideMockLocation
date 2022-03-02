# Hide Mock Locations
[![Release](https://img.shields.io/github/v/release/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation?label=Release)](https://github.com/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation/releases/latest)
[![Stars](https://img.shields.io/github/stars/ThePieMonster/HideMockLocation?label=Stars)](https://github.com/ThePieMonster)
[![Download](https://img.shields.io/github/downloads/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation/total)](https://github.com/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation/releases/latest)
[![GitHub license](https://img.shields.io/github/license/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation)](https://github.com/Xposed-Modules-Repo/com.github.thepiemonster.hidemocklocation/blob/main/LICENSE)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ThePieMonster/HideMockLocation/Java%20CI%20with%20Gradle)](https://github.com/ThePieMonster/HideMockLocation/actions)

Hide Mock Location is an Xposed Module (now LSPosed on Android 11), which hides information about the 'Allow mock locations' setting.

## Usage
* Install module from [LSPosed](https://github.com/LSPosed/LSPosed) module store.
* Enable module in LSPosed (select System Framework) and reboot device.
* That's it! You can open Hide Mock Locations and view the "Test Location Data" page to view the status of the mock location setting.

## Tips
* You can view the "Test Location Data" page without enabling the module in LSPosed.
* Try enabling a mock location application before and after enabling the LSPosed module to view different output settings.
