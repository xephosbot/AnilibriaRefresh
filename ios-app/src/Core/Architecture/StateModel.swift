//
//  StateModel.swift
//  AnilibriaRefresh
//
//  Observes a KMP Orbit `ContainerHost` state flow and exposes it as a typed, non-null,
//  observable value for SwiftUI.
//
//  Orbit's `Container` is exported as a non-generic Obj-C protocol, so `container.stateFlow`
//  arrives with an erased element type. SKIE still bridges it to a `SkieSwiftStateFlow`
//  (an `AsyncSequence` with a non-null `.value`), so we seed from `.value` and stream updates,
//  casting each emission to the concrete state type in exactly one place.
//

import Foundation
import Shared

@MainActor
@Observable
final class StateModel<State: AnyObject> {
    private(set) var state: State

    @ObservationIgnored
    private var task: Task<Void, Never>?

    /// - Parameter stateFlow: a view model's `container.stateFlow`. The generic `Element` is
    ///   inferred from SKIE's bridged type (erased), so callers only specify the concrete `State`.
    init<Element>(_ stateFlow: SkieSwiftStateFlow<Element>) {
        // A `StateFlow` always holds a current value, so this is non-null and safe.
        state = stateFlow.value as! State
        task = Task { @MainActor [weak self] in
            for await value in stateFlow {
                self?.state = value as! State
            }
        }
    }

    deinit {
        task?.cancel()
    }
}
