# Debug Build Notes

This project is configured to build debug APKs without requiring manual keystore setup.

## Debug Keystore

Android automatically uses a debug keystore located at:
- `~/.android/debug.keystore`

The debug keystore has these default properties:
- **Keystore name**: debug.keystore
- **Keystore password**: android
- **Key alias**: androiddebugkey
- **Key password**: android
- **CN**: CN=Android Debug,O=Android,C=US

## Building

For debug builds (development/testing):
```bash
./gradlew assembleDebug
```

For release builds (requires custom keystore):
```bash
./gradlew assembleRelease
```

## Cloud Build Services

All cloud build services (Codemagic, GitHub Actions, etc.) are configured to build debug APKs which don't require custom keystores.

For release builds on cloud services, you would need to:
1. Generate a release keystore
2. Upload it to the cloud service's secure storage
3. Configure the signing credentials in the build configuration