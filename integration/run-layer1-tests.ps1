$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
Push-Location $repoRoot
try {
    & .\mvnw.cmd verify "-Dgroups=Layer1"
    $testExit = $LASTEXITCODE
}
finally {
    Pop-Location
}

if ($testExit -eq 0) {
    Write-Host ""
    Write-Host "✅ Layer 1 integration tests PASSED"
}
else {
    Write-Host ""
    Write-Host "❌ Layer 1 integration tests FAILED"
}
exit $testExit
