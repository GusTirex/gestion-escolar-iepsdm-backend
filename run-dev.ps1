# ============================================================
# run-dev.ps1  -  Levanta el backend en modo desarrollo
# Carga las variables del archivo .env y arranca Spring Boot.
# Necesario porque spring-dotenv no es compatible con Spring Boot 4,
# asi que cargamos el .env manualmente en el entorno del proceso.
#
# Uso:   .\run-dev.ps1
# ============================================================

$ErrorActionPreference = "Stop"
Set-Location -Path $PSScriptRoot

if (-not (Test-Path ".env")) {
    Write-Host "[ERROR] No existe el archivo .env en $PSScriptRoot" -ForegroundColor Red
    Write-Host "        Copia .env.example a .env y ajusta tus credenciales." -ForegroundColor Yellow
    exit 1
}

Write-Host "[run-dev] Cargando variables desde .env ..." -ForegroundColor Cyan
Get-Content ".env" | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
        $idx  = $line.IndexOf("=")
        $name = $line.Substring(0, $idx).Trim()
        $val  = $line.Substring($idx + 1).Trim()
        [Environment]::SetEnvironmentVariable($name, $val, "Process")
        if ($name -notmatch "PASSWORD|KEY") {
            Write-Host ("   {0} = {1}" -f $name, $val) -ForegroundColor DarkGray
        } else {
            Write-Host ("   {0} = ****" -f $name) -ForegroundColor DarkGray
        }
    }
}

Write-Host "[run-dev] Iniciando Spring Boot (perfil dev)..." -ForegroundColor Cyan
& ".\mvnw.cmd" -DskipTests spring-boot:run
